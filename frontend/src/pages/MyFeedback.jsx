import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { feedbackAPI } from '../services/api';
import FeedbackCard from '../components/FeedbackCard';
import './MyFeedback.css';

const MyFeedback = () => {
  const [feedbacks, setFeedbacks] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchMyFeedback();
  }, []);

  const fetchMyFeedback = async () => {
    try {
      const response = await feedbackAPI.getMyFeedback();
      setFeedbacks(response.data);
    } catch (err) {
      console.error('Failed to load feedback', err);
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async (id) => {
    if (window.confirm('Are you sure you want to delete this feedback?')) {
      await feedbackAPI.delete(id);
      fetchMyFeedback();
    }
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  return (
    <div className="my-feedback-page">
      <div className="page-header">
        <h1>My Feedback</h1>
        <p>View and manage your course reviews</p>
      </div>

      {feedbacks.length > 0 ? (
        <div className="feedback-list">
          {feedbacks.map((feedback) => (
            <div key={feedback.id} className="feedback-item">
              <Link to={`/courses/${feedback.courseId}`} className="course-link">
                📚 {feedback.courseName}
              </Link>
              <FeedbackCard
                feedback={feedback}
                onDelete={handleDelete}
                canDelete={true}
              />
            </div>
          ))}
        </div>
      ) : (
        <div className="empty-state">
          <p>You haven't submitted any feedback yet.</p>
          <Link to="/courses" className="browse-link">
            Browse Courses
          </Link>
        </div>
      )}
    </div>
  );
};

export default MyFeedback;
