import { useState, useEffect } from 'react';

const API = 'http://localhost:3001/api';

export default function CourseList({ onReviews }) {
  const [courses, setCourses] = useState([]);
  const [username, setUsername] = useState('');
  const [form, setForm] = useState({ title: '', description: '', instructor: '' });

  const load = () =>
    fetch(`${API}/courses`).then(r => r.json()).then(setCourses);

  useEffect(() => { load(); }, []);

  const enroll = async (id) => {
    if (!username) return alert('Введіть ім\'я!');
    await fetch(`${API}/courses/${id}/enroll`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username }),
    });
    load();
  };

  const addCourse = async () => {
    if (!form.title) return;
    await fetch(`${API}/courses`, {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(form),
    });
    setForm({ title: '', description: '', instructor: '' });
    load();
  };

  const stars = (rating) => rating ? '★'.repeat(Math.round(rating)) + '☆'.repeat(5 - Math.round(rating)) : '—';

  return (
    <div>
      <div style={{ marginBottom: 20 }}>
        <input placeholder="Ваше ім'я для запису" value={username}
          onChange={e => setUsername(e.target.value)}
          style={{ padding: 8, marginRight: 8, borderRadius: 4, border: '1px solid #ccc', width: 220 }} />
      </div>

      <div style={{ display: 'grid', gridTemplateColumns: 'repeat(auto-fill, minmax(260px, 1fr))', gap: 16, marginBottom: 32 }}>
        {courses.map(c => (
          <div key={c.id} style={{ background: '#fff', borderRadius: 10, padding: 20, boxShadow: '0 2px 8px #0001' }}>
            <h3 style={{ margin: '0 0 8px', color: '#4a6cf7' }}>{c.title}</h3>
            <p style={{ color: '#666', margin: '0 0 6px', fontSize: 14 }}>{c.description}</p>
            <p style={{ margin: '0 0 6px', fontSize: 13 }}>👨🏫 {c.instructor}</p>
            <p style={{ margin: '0 0 6px', fontSize: 13 }}>👥 Записано: {c.enrolledCount}</p>
            <p style={{ margin: '0 0 12px', fontSize: 13, color: '#f5a623' }}>
              {stars(c.avgRating)} {c.avgRating ? `(${c.avgRating})` : ''}
            </p>
            <button onClick={() => enroll(c.id)}
              style={{ marginRight: 8, padding: '6px 14px', background: '#4a6cf7', color: '#fff', border: 'none', borderRadius: 5, cursor: 'pointer' }}>
              Записатися
            </button>
            <button onClick={() => onReviews(c)}
              style={{ padding: '6px 14px', background: '#eee', border: 'none', borderRadius: 5, cursor: 'pointer' }}>
              Відгуки
            </button>
          </div>
        ))}
      </div>

      <h2>Додати курс</h2>
      <div style={{ background: '#fff', padding: 20, borderRadius: 10, maxWidth: 400, boxShadow: '0 2px 8px #0001' }}>
        {['title', 'description', 'instructor'].map(f => (
          <input key={f} placeholder={{ title: 'Назва', description: 'Опис', instructor: 'Викладач' }[f]}
            value={form[f]} onChange={e => setForm({ ...form, [f]: e.target.value })}
            style={{ display: 'block', width: '100%', marginBottom: 10, padding: 8, borderRadius: 4, border: '1px solid #ccc', boxSizing: 'border-box' }} />
        ))}
        <button onClick={addCourse}
          style={{ width: '100%', padding: 10, background: '#4a6cf7', color: '#fff', border: 'none', borderRadius: 5, cursor: 'pointer' }}>
          Додати курс
        </button>
      </div>
    </div>
  );
}
