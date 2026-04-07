import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { coursesAPI } from '../services/api';
import StarRating from '../components/StarRating';
import './Courses.css';

const Courses = () => {
  const [courses, setCourses] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchCourses();
  }, []);

  const fetchCourses = async () => {
    try {
      const response = await coursesAPI.getAll();
      setCourses(response.data);
    } catch (err) {
      setError('Failed to load courses');
    } finally {
      setLoading(false);
    }
  };

  if (loading) {
    return <div className="loading">Loading courses...</div>;
  }

  return (
    <div className="courses-page">
      <div className="page-header">
        <h1>Browse Courses</h1>
        <p>Explore courses and share your feedback</p>
      </div>

      {error && <div className="error-message">{error}</div>}

      <div className="courses-grid">
        {courses.map((course) => (
          <Link to={`/courses/${course.id}`} key={course.id} className="course-card">
            <div className="course-code">{course.code || 'COURSE'}</div>
            <h3>{course.name}</h3>
            <p className="course-description">
              {course.description || 'No description available'}
            </p>
            
            <div className="course-meta">
              {course.instructorName && (
                <span className="instructor">👨‍🏫 {course.instructorName}</span>
              )}
              <div className="rating-info">
                <StarRating rating={course.averageRating || 0} readonly size="small" />
                <span className="feedback-count">
                  ({course.feedbackCount || 0} reviews)
                </span>
              </div>
            </div>
          </Link>
        ))}
      </div>

      {courses.length === 0 && !error && (
        <div className="empty-state">
          <p>No courses available yet.</p>
        </div>
      )}
    </div>
  );
};

export default Courses;
