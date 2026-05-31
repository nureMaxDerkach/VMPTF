package com.example.shop

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.shop.models.*

class DatabaseHelper(context: Context) :
    SQLiteOpenHelper(context, "shop.db", null, 1) {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL("PRAGMA foreign_keys = ON")

        db.execSQL("""CREATE TABLE categories (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            description TEXT)""")

        db.execSQL("""CREATE TABLE users (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            name TEXT NOT NULL,
            email TEXT UNIQUE NOT NULL,
            phone TEXT,
            created_at TEXT DEFAULT (datetime('now')))""")

        db.execSQL("""CREATE TABLE products (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            category_id INTEGER NOT NULL,
            name TEXT NOT NULL,
            description TEXT,
            price REAL NOT NULL,
            stock INTEGER DEFAULT 0,
            FOREIGN KEY (category_id) REFERENCES categories(id))""")

        db.execSQL("""CREATE TABLE orders (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            status TEXT DEFAULT 'pending',
            total REAL DEFAULT 0,
            created_at TEXT DEFAULT (datetime('now')),
            FOREIGN KEY (user_id) REFERENCES users(id))""")

        db.execSQL("""CREATE TABLE order_items (
            order_id INTEGER NOT NULL,
            product_id INTEGER NOT NULL,
            quantity INTEGER DEFAULT 1,
            price REAL NOT NULL,
            PRIMARY KEY (order_id, product_id),
            FOREIGN KEY (order_id) REFERENCES orders(id),
            FOREIGN KEY (product_id) REFERENCES products(id))""")

        db.execSQL("""CREATE TABLE reviews (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            user_id INTEGER NOT NULL,
            product_id INTEGER NOT NULL,
            rating INTEGER NOT NULL,
            comment TEXT,
            created_at TEXT DEFAULT (datetime('now')),
            FOREIGN KEY (user_id) REFERENCES users(id),
            FOREIGN KEY (product_id) REFERENCES products(id))""")

        seedData(db)
    }

    override fun onUpgrade(db: SQLiteDatabase, old: Int, new: Int) {
        listOf("reviews","order_items","orders","products","users","categories")
            .forEach { db.execSQL("DROP TABLE IF EXISTS $it") }
        onCreate(db)
    }

    private fun seedData(db: SQLiteDatabase) {
        listOf("Електроніка", "Одяг", "Книги", "Спорт").forEachIndexed { i, name ->
            val cv = ContentValues().apply {
                put("name", name); put("description", "Категорія: $name")
            }
            db.insert("categories", null, cv)
        }
        listOf(
            Triple("Деркач Максим", "derkach@mail.com", "+380501234567"),
            Triple("Іваненко Олена", "ivan@mail.com",   "+380671234567"),
        ).forEach { (name, email, phone) ->
            val cv = ContentValues().apply {
                put("name", name); put("email", email); put("phone", phone)
            }
            db.insert("users", null, cv)
        }
        listOf(
            Triple(1, "Смартфон Samsung", 12999.0),
            Triple(1, "Навушники Sony",   2499.0),
            Triple(2, "Куртка зимова",    1800.0),
            Triple(3, "Kotlin у дії",      650.0),
            Triple(4, "Гантелі 10 кг",    1200.0),
        ).forEach { (catId, name, price) ->
            val cv = ContentValues().apply {
                put("category_id", catId); put("name", name)
                put("price", price); put("stock", 10)
            }
            db.insert("products", null, cv)
        }
    }

    fun getAllCategories(): List<Category> {
        val list = mutableListOf<Category>()
        val c = readableDatabase.rawQuery("SELECT * FROM categories", null)
        while (c.moveToNext())
            list.add(Category(c.getInt(0), c.getString(1), c.getString(2) ?: ""))
        c.close(); return list
    }

    fun addCategory(category: Category): Long {
        val cv = ContentValues().apply {
            put("name", category.name)
            put("description", category.description)
        }
        return writableDatabase.insert("categories", null, cv)
    }

    fun updateCategory(category: Category): Int {
        val cv = ContentValues().apply {
            put("name", category.name)
            put("description", category.description)
        }
        return writableDatabase.update("categories", cv,
            "id = ?", arrayOf(category.id.toString()))
    }

    fun deleteCategory(id: Int): Int =
        writableDatabase.delete("categories", "id = ?", arrayOf(id.toString()))

    fun getAllUsers(): List<User> {
        val list = mutableListOf<User>()
        val c = readableDatabase.rawQuery("SELECT * FROM users", null)
        while (c.moveToNext())
            list.add(User(c.getInt(0), c.getString(1), c.getString(2),
                c.getString(3) ?: "", c.getString(4) ?: ""))
        c.close(); return list
    }

    fun addUser(user: User): Long {
        val cv = ContentValues().apply {
            put("name", user.name); put("email", user.email); put("phone", user.phone)
        }
        return writableDatabase.insert("users", null, cv)
    }

    fun updateUser(user: User): Int {
        val cv = ContentValues().apply {
            put("name", user.name); put("email", user.email); put("phone", user.phone)
        }
        return writableDatabase.update("users", cv, "id = ?", arrayOf(user.id.toString()))
    }

    fun deleteUser(id: Int): Int =
        writableDatabase.delete("users", "id = ?", arrayOf(id.toString()))

    fun getAllProducts(): List<Product> {
        val list = mutableListOf<Product>()
        val c = readableDatabase.rawQuery("""
            SELECT p.id, p.category_id, p.name, p.description,
                   p.price, p.stock, c.name
            FROM products p
            JOIN categories c ON p.category_id = c.id""", null)
        while (c.moveToNext())
            list.add(Product(c.getInt(0), c.getInt(1), c.getString(2),
                c.getString(3) ?: "", c.getDouble(4), c.getInt(5), c.getString(6)))
        c.close(); return list
    }

    fun addProduct(product: Product): Long {
        val cv = ContentValues().apply {
            put("category_id", product.categoryId); put("name", product.name)
            put("description", product.description)
            put("price", product.price); put("stock", product.stock)
        }
        return writableDatabase.insert("products", null, cv)
    }

    fun updateProduct(product: Product): Int {
        val cv = ContentValues().apply {
            put("name", product.name); put("price", product.price)
            put("stock", product.stock); put("description", product.description)
        }
        return writableDatabase.update("products", cv, "id = ?",
            arrayOf(product.id.toString()))
    }

    fun deleteProduct(id: Int): Int =
        writableDatabase.delete("products", "id = ?", arrayOf(id.toString()))

    fun getAllOrders(): List<Order> {
        val list = mutableListOf<Order>()
        val c = readableDatabase.rawQuery("""
            SELECT o.id, o.user_id, o.status, o.total,
                   o.created_at, u.name
            FROM orders o
            JOIN users u ON o.user_id = u.id
            ORDER BY o.id DESC""", null)
        while (c.moveToNext())
            list.add(Order(c.getInt(0), c.getInt(1), c.getString(2),
                c.getDouble(3), c.getString(4), c.getString(5)))
        c.close(); return list
    }

    fun addOrder(order: Order, items: List<OrderItem>): Long {
        val total = items.sumOf { it.price * it.quantity }
        val cv = ContentValues().apply {
            put("user_id", order.userId)
            put("status", order.status)
            put("total", total)
        }
        val orderId = writableDatabase.insert("orders", null, cv)
        items.forEach { item ->
            val icv = ContentValues().apply {
                put("order_id", orderId); put("product_id", item.productId)
                put("quantity", item.quantity); put("price", item.price)
            }
            writableDatabase.insert("order_items", null, icv)
        }
        return orderId
    }

    fun updateOrderStatus(orderId: Int, status: String): Int {
        val cv = ContentValues().apply { put("status", status) }
        return writableDatabase.update("orders", cv, "id = ?",
            arrayOf(orderId.toString()))
    }

    fun deleteOrder(id: Int): Int {
        writableDatabase.delete("order_items", "order_id = ?", arrayOf(id.toString()))
        return writableDatabase.delete("orders", "id = ?", arrayOf(id.toString()))
    }

    fun getProductReviews(productId: Int): List<Review> {
        val list = mutableListOf<Review>()
        val c = readableDatabase.rawQuery("""
            SELECT r.id, r.user_id, r.product_id, r.rating,
                   r.comment, r.created_at, u.name, p.name
            FROM reviews r
            JOIN users u ON r.user_id = u.id
            JOIN products p ON r.product_id = p.id
            WHERE r.product_id = ?""", arrayOf(productId.toString()))
        while (c.moveToNext())
            list.add(Review(c.getInt(0), c.getInt(1), c.getInt(2),
                c.getInt(3), c.getString(4) ?: "", c.getString(5),
                c.getString(6), c.getString(7)))
        c.close(); return list
    }

    fun addReview(review: Review): Long {
        val cv = ContentValues().apply {
            put("user_id", review.userId); put("product_id", review.productId)
            put("rating", review.rating); put("comment", review.comment)
        }
        return writableDatabase.insert("reviews", null, cv)
    }

    fun updateReview(review: Review): Int {
        val cv = ContentValues().apply {
            put("rating", review.rating); put("comment", review.comment)
        }
        return writableDatabase.update("reviews", cv, "id = ?",
            arrayOf(review.id.toString()))
    }

    fun deleteReview(id: Int): Int =
        writableDatabase.delete("reviews", "id = ?", arrayOf(id.toString()))
}
