import StarRating from './StarRating';
import './CourseStats.css';

const CourseStats = ({ stats }) => {
  if (!stats) return null;

  const distribution = stats.ratingDistribution || {};
  const total = stats.totalFeedbacks || 0;

  const getPercentage = (count) => {
    if (total === 0) return 0;
    return ((count || 0) / total) * 100;
  };

  return (
    <div className="course-stats">
      <div className="stats-overview">
        <div className="average-rating">
          <span className="rating-number">
            {stats.averageRating?.toFixed(1) || 'N/A'}
          </span>
          <StarRating rating={stats.averageRating || 0} readonly size="medium" />
          <span className="total-reviews">{total} reviews</span>
        </div>

        <div className="rating-bars">
          {[5, 4, 3, 2, 1].map((star) => {
            const count = distribution[`${['one', 'two', 'three', 'four', 'five'][star - 1]}Star`] || 0;
            const percentage = getPercentage(count);
            
            return (
              <div key={star} className="rating-bar">
                <span className="star-label">{star} ★</span>
                <div className="bar-container">
                  <div
                    className="bar-fill"
                    style={{ width: `${percentage}%` }}
                  />
                </div>
                <span className="count">{count}</span>
              </div>
            );
          })}
        </div>
      </div>

      <div className="detailed-stats">
        <div className="stat-item">
          <span className="stat-label">Content Quality</span>
          <StarRating rating={stats.averageContentRating || 0} readonly size="small" />
        </div>
        <div className="stat-item">
          <span className="stat-label">Instructor</span>
          <StarRating rating={stats.averageInstructorRating || 0} readonly size="small" />
        </div>
        <div className="stat-item">
          <span className="stat-label">Difficulty</span>
          <StarRating rating={stats.averageDifficultyRating || 0} readonly size="small" />
        </div>
      </div>
    </div>
  );
};

export default CourseStats;
