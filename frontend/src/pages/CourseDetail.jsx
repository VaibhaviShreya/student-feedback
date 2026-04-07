import { useState, useEffect } from 'react';
import { useParams } from 'react-router-dom';
import { coursesAPI, feedbackAPI } from '../services/api';
import { useAuth } from '../context/AuthContext';
import FeedbackForm from '../components/FeedbackForm';
import FeedbackCard from '../components/FeedbackCard';
import CourseStats from '../components/CourseStats';
import './CourseDetail.css';

const CourseDetail = () => {
  const { id } = useParams();
  const { isAuthenticated, user } = useAuth();
  
  const [course, setCourse] = useState(null);
  const [feedbacks, setFeedbacks] = useState([]);
  const [stats, setStats] = useState(null);
  const [loading, setLoading] = useState(true);
  const [showFeedbackForm, setShowFeedbackForm] = useState(false);
  const [hasSubmitted, setHasSubmitted] = useState(false);

  useEffect(() => {
    fetchData();
  }, [id]);

  const fetchData = async () => {
    try {
      const [courseRes, feedbackRes, statsRes] = await Promise.all([
        coursesAPI.getById(id),
        feedbackAPI.getByCourse(id),
        feedbackAPI.getCourseStats(id),
      ]);
      
      setCourse(courseRes.data);
      setFeedbacks(feedbackRes.data);
      setStats(statsRes.data);
      
      // Check if current user has already submitted feedback
      if (user) {
        const userFeedback = feedbackRes.data.find(
          (f) => f.studentName === user.username || 
                 (f.isAnonymous && feedbackRes.data.some(
                   (fb) => fb.studentId === user.id
                 ))
        );
        setHasSubmitted(!!userFeedback);
      }
    } catch (err) {
      console.error('Failed to load course data', err);
    } finally {
      setLoading(false);
    }
  };

  const handleSubmitFeedback = async (data) => {
    await feedbackAPI.create(data);
    setShowFeedbackForm(false);
    setHasSubmitted(true);
    fetchData();
  };

  const handleDeleteFeedback = async (feedbackId) => {
    if (window.confirm('Are you sure you want to delete this feedback?')) {
      await feedbackAPI.delete(feedbackId);
      fetchData();
    }
  };

  if (loading) {
    return <div className="loading">Loading...</div>;
  }

  if (!course) {
    return <div className="error-state">Course not found</div>;
  }

  return (
    <div className="course-detail-page">
      <div className="course-header">
        <div className="course-info">
          <span className="course-code">{course.code || 'COURSE'}</span>
          <h1>{course.name}</h1>
          <p>{course.description}</p>
          {course.instructorName && (
            <p className="instructor">Instructor: {course.instructorName}</p>
          )}
        </div>
      </div>

      <div className="course-content">
        <div className="main-content">
          <section className="stats-section">
            <h2>Course Ratings</h2>
            <CourseStats stats={stats} />
          </section>

          {isAuthenticated && !hasSubmitted && (
            <section className="feedback-form-section">
              {showFeedbackForm ? (
                <>
                  <h2>Submit Your Feedback</h2>
                  <FeedbackForm
                    courseId={parseInt(id)}
                    onSubmit={handleSubmitFeedback}
                  />
                  <button
                    className="cancel-btn"
                    onClick={() => setShowFeedbackForm(false)}
                  >
                    Cancel
                  </button>
                </>
              ) : (
                <button
                  className="add-feedback-btn"
                  onClick={() => setShowFeedbackForm(true)}
                >
                  ✍️ Write a Review
                </button>
              )}
            </section>
          )}

          {hasSubmitted && (
            <div className="already-submitted">
              ✅ You have already submitted feedback for this course
            </div>
          )}

          <section className="feedbacks-section">
            <h2>Student Reviews ({feedbacks.length})</h2>
            {feedbacks.length > 0 ? (
              <div className="feedbacks-list">
                {feedbacks.map((feedback) => (
                  <FeedbackCard
                    key={feedback.id}
                    feedback={feedback}
                    onDelete={handleDeleteFeedback}
                    canDelete={
                      user && 
                      (user.role === 'ADMIN' || 
                       feedback.studentName === user.username)
                    }
                  />
                ))}
              </div>
            ) : (
              <p className="no-feedbacks">No reviews yet. Be the first to share your experience!</p>
            )}
          </section>
        </div>
      </div>
    </div>
  );
};

export default CourseDetail;
