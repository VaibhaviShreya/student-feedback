import StarRating from './StarRating';
import './FeedbackCard.css';

const FeedbackCard = ({ feedback, onDelete, canDelete }) => {
  const formatDate = (dateString) => {
    return new Date(dateString).toLocaleDateString('en-US', {
      year: 'numeric',
      month: 'short',
      day: 'numeric',
    });
  };

  return (
    <div className="feedback-card">
      <div className="feedback-header">
        <div className="feedback-author">
          <div className="avatar">
            {feedback.isAnonymous ? '?' : feedback.studentName?.charAt(0) || 'U'}
          </div>
          <div className="author-info">
            <span className="author-name">
              {feedback.isAnonymous ? 'Anonymous' : feedback.studentName}
            </span>
            <span className="feedback-date">{formatDate(feedback.createdAt)}</span>
          </div>
        </div>
        <StarRating rating={feedback.rating} readonly size="small" />
      </div>

      {(feedback.contentRating || feedback.instructorRating || feedback.difficultyRating) && (
        <div className="sub-ratings">
          {feedback.contentRating && (
            <div className="sub-rating">
              <span>Content:</span>
              <StarRating rating={feedback.contentRating} readonly size="small" />
            </div>
          )}
          {feedback.instructorRating && (
            <div className="sub-rating">
              <span>Instructor:</span>
              <StarRating rating={feedback.instructorRating} readonly size="small" />
            </div>
          )}
          {feedback.difficultyRating && (
            <div className="sub-rating">
              <span>Difficulty:</span>
              <StarRating rating={feedback.difficultyRating} readonly size="small" />
            </div>
          )}
        </div>
      )}

      {feedback.comment && (
        <p className="feedback-comment">{feedback.comment}</p>
      )}

      {canDelete && (
        <button className="delete-btn" onClick={() => onDelete(feedback.id)}>
          Delete
        </button>
      )}
    </div>
  );
};

export default FeedbackCard;
