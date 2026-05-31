import { useState, useEffect } from 'react';

const API = 'http://localhost:3001/api';

export default function Reviews({ course }) {
  const [reviews, setReviews] = useState([]);
  const [form, setForm] = useState({ username: '', rating: 5, comment: '' });

  const load = () =>
    fetch(`${API}/courses/${course.id}/reviews`).then(r => r.json()).then(setReviews);

  useEffect(() => { load(); }, [course.id]);

  const submit = async () => {
    if (!form.username || !form.comment) return alert('Заповніть всі поля!');
    await fetch(`${API}/courses/${course.id}/reviews`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form),
    });
    setForm({ username: '', rating: 5, comment: '' });
    load();
  };

  const stars = (n) => '★'.repeat(n) + '☆'.repeat(5 - n);

  return (
    <div>
      <h2>Відгуки: {course.title}</h2>

      {reviews.length === 0
        ? <p style={{ color: '#888' }}>Поки немає відгуків.</p>
        : reviews.map((r, i) => (
          <div key={i} style={{ background: '#fff', borderRadius: 8, padding: 16, marginBottom: 12, boxShadow: '0 2px 6px #0001' }}>
            <strong>{r.username}</strong>
            <span style={{ color: '#f5a623', marginLeft: 10 }}>{stars(r.rating)}</span>
            <p style={{ margin: '8px 0 0', color: '#555' }}>{r.comment}</p>
          </div>
        ))
      }

      <h3 style={{ marginTop: 24 }}>Залишити відгук</h3>
      <div style={{ background: '#fff', padding: 20, borderRadius: 10, maxWidth: 400, boxShadow: '0 2px 8px #0001' }}>
        <input placeholder="Ваше ім'я" value={form.username}
          onChange={e => setForm({ ...form, username: e.target.value })}
          style={{ display: 'block', width: '100%', marginBottom: 10, padding: 8, borderRadius: 4, border: '1px solid #ccc', boxSizing: 'border-box' }} />
        <label style={{ display: 'block', marginBottom: 6 }}>
          Оцінка: <strong style={{ color: '#f5a623' }}>{stars(form.rating)}</strong>
          <input type="range" min="1" max="5" value={form.rating}
            onChange={e => setForm({ ...form, rating: parseInt(e.target.value) })}
            style={{ width: '100%', marginTop: 4 }} />
        </label>
        <textarea placeholder="Ваш відгук" value={form.comment}
          onChange={e => setForm({ ...form, comment: e.target.value })}
          rows={3}
          style={{ display: 'block', width: '100%', marginBottom: 10, padding: 8, borderRadius: 4, border: '1px solid #ccc', boxSizing: 'border-box' }} />
        <button onClick={submit}
          style={{ width: '100%', padding: 10, background: '#4a6cf7', color: '#fff', border: 'none', borderRadius: 5, cursor: 'pointer' }}>
          Надіслати відгук
        </button>
      </div>
    </div>
  );
}
