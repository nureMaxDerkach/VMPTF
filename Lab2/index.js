const express = require('express');
const cors = require('cors');
const app = express();

app.use(cors());
app.use(express.json());

let courses = [
  { id: 1, title: 'Python для початківців', description: 'Основи мови Python', instructor: 'Іваненко О.', enrolled: [] },
  { id: 2, title: 'JavaScript та React',    description: 'Сучасна веб-розробка',  instructor: 'Петренко В.', enrolled: [] },
  { id: 3, title: 'Django Framework',       description: 'Веб-розробка на Python', instructor: 'Сидоренко М.', enrolled: [] },
];

let reviews = {};
let nextId = 4;

app.get('/api/courses', (req, res) => {
  const result = courses.map(c => ({
    ...c,
    enrolledCount: c.enrolled.length,
    avgRating: reviews[c.id]?.length
      ? (reviews[c.id].reduce((s, r) => s + r.rating, 0) / reviews[c.id].length).toFixed(1)
      : null,
  }));
  res.json(result);
});

app.post('/api/courses', (req, res) => {
  const { title, description, instructor } = req.body;
  const course = { id: nextId++, title, description, instructor, enrolled: [] };
  courses.push(course);
  res.json(course);
});

app.post('/api/courses/:id/enroll', (req, res) => {
  const course = courses.find(c => c.id === parseInt(req.params.id));
  if (!course) return res.status(404).json({ error: 'Курс не знайдено' });
  const { username } = req.body;
  if (!course.enrolled.includes(username)) course.enrolled.push(username);
  res.json({ message: `${username} записаний на курс`, enrolledCount: course.enrolled.length });
});

app.get('/api/courses/:id/reviews', (req, res) => {
  const id = parseInt(req.params.id);
  res.json(reviews[id] || []);
});

app.post('/api/courses/:id/reviews', (req, res) => {
  const id = parseInt(req.params.id);
  const { username, rating, comment } = req.body;
  if (!reviews[id]) reviews[id] = [];
  reviews[id].push({ username, rating: parseInt(rating), comment });
  res.json({ message: 'Відгук додано' });
});

app.listen(3001, () => console.log('Server on port 3001'));
