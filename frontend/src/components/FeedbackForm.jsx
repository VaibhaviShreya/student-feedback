import { useState } from 'react';
import StarRating from './StarRating';
import './FeedbackForm.css';

const FeedbackForm = ({ courseId, onSubmit, initialData = null }) => {
  const [formData, setFormData] = useState({
    courseId: courseId,
    rating: initialData?.rating || 0,
    contentRating: initialData?.contentRating || 0,
    instructorRating: initialData?.instructorRating || 0,
    difficultyRating: initialData?.difficultyRating || 0,
    comment: initialData?.comment || '',
    isAnonymous: initialData?.isAnonymous || false,
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');

  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (formData.rating === 0) {
      setError('Please provide an overall rating');
      return;
    }

    setLoading(true);
    setError('');

    try {
      await onSubmit(formData);
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to submit feedback');
    } finally {
      setLoading(false);
    }
  };

  const updateRating = (field, value) => {
    setFormData(prev => ({ ...prev, [field]: value }));
  };

  return (
    <form className="feedback-form" onSubmit={handleSubmit}>
      {error && <div className="error-message">{error}</div>}

      <div className="rating-section">
        <label>Overall Rating *</label>
        <StarRating
          rating={formData.rating}
          onRatingChange={(value) => updateRating('rating', value)}
          size="large"
        />
      </div>

      <div className="rating-group">
        <div className="rating-item">
          <label>Content Quality</label>
          <StarRating
            rating={formData.contentRating}
            onRatingChange={(value) => updateRating('contentRating', value)}
          />
        </div>

        <div className="rating-item">
          <label>Instructor</label>
          <StarRating
            rating={formData.instructorRating}
            onRatingChange={(value) => updateRating('instructorRating', value)}
          />
        </div>

        <div className="rating-item">
          <label>Difficulty Level</label>
          <StarRating
            rating={formData.difficultyRating}
            onRatingChange={(value) => updateRating('difficultyRating', value)}
          />
        </div>
      </div>

      <div className="form-group">
        <label htmlFor="comment">Your Feedback</label>
        <textarea
          id="comment"
          value={formData.comment}
          onChange={(e) => setFormData(prev => ({ ...prev, comment: e.target.value }))}
          placeholder="Share your experience with this course..."
          rows={4}
          maxLength={1000}
        />
        <span className="char-count">{formData.comment.length}/1000</span>
      </div>

      <div className="form-group checkbox-group">
        <label>
          <input
            type="checkbox"
            checked={formData.isAnonymous}
            onChange={(e) => setFormData(prev => ({ ...prev, isAnonymous: e.target.checked }))}
          />
          Submit anonymously
        </label>
      </div>

      <button type="submit" className="submit-btn" disabled={loading}>
        {loading ? 'Submitting...' : initialData ? 'Update Feedback' : 'Submit Feedback'}
      </button>
    </form>
  );
};

export default FeedbackForm;
