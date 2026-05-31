import { useState } from 'react';
import CourseList from './CourseList';
import Reviews from './Reviews';

export default function App() {
  const [page, setPage] = useState('courses');
  const [selectedCourse, setSelectedCourse] = useState(null);

  const openReviews = (course) => {
    setSelectedCourse(course);
    setPage('reviews');
  };

  return (
    <div style={{ fontFamily: 'Arial, sans-serif', maxWidth: 900, margin: '0 auto', padding: 24 }}>
      <h1>📚 Каталог курсів</h1>
      <nav style={{ marginBottom: 24 }}>
        <button onClick={() => setPage('courses')}
          style={{ marginRight: 12, padding: '8px 20px',
            background: page === 'courses' ? '#4a6cf7' : '#eee',
            color: page === 'courses' ? '#fff' : '#333',
            border: 'none', borderRadius: 6, cursor: 'pointer' }}>
          Курси
        </button>
        {selectedCourse && (
          <button onClick={() => setPage('reviews')}
            style={{ padding: '8px 20px',
              background: page === 'reviews' ? '#4a6cf7' : '#eee',
              color: page === 'reviews' ? '#fff' : '#333',
              border: 'none', borderRadius: 6, cursor: 'pointer' }}>
            Відгуки: {selectedCourse.title}
          </button>
        )}
      </nav>
      {page === 'courses' && <CourseList onReviews={openReviews} />}
      {page === 'reviews' && selectedCourse && <Reviews course={selectedCourse} />}
    </div>
  );
}
