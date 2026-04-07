import { useEffect, useMemo, useState } from 'react'
import client from '../api/client'
import { useAuth } from '../context/AuthContext'

function DashboardPage() {
  const { logout } = useAuth()
  const [courses, setCourses] = useState([])
  const [feedback, setFeedback] = useState([])
  const [loading, setLoading] = useState(true)
  const [error, setError] = useState('')
  const [query, setQuery] = useState('')
  const [form, setForm] = useState({ courseId: '', rating: 5, comment: '' })

  const loadData = async () => {
    try {
      setError('')
      const [coursesRes, feedbackRes] = await Promise.all([
        client.get('/courses'),
        client.get('/feedback'),
      ])
      setCourses(coursesRes.data)
      setFeedback(feedbackRes.data)
      if (coursesRes.data.length && !form.courseId) {
        setForm((prev) => ({ ...prev, courseId: String(coursesRes.data[0].id) }))
      }
    } catch (err) {
      if (err?.response?.status === 401 || err?.response?.status === 403) {
        logout()
      } else {
        setError('Could not load courses/feedback right now.')
      }
    } finally {
      setLoading(false)
    }
  }

  useEffect(() => {
    loadData()
  }, [])

  const filteredFeedback = useMemo(() => {
    const q = query.trim().toLowerCase()
    if (!q) return feedback
    return feedback.filter((item) =>
      item.courseCode.toLowerCase().includes(q) ||
      item.courseTitle.toLowerCase().includes(q) ||
      item.studentUsername.toLowerCase().includes(q) ||
      (item.comment || '').toLowerCase().includes(q)
    )
  }, [feedback, query])

  const submitFeedback = async (e) => {
    e.preventDefault()
    try {
      await client.post('/feedback', {
        courseId: Number(form.courseId),
        rating: Number(form.rating),
        comment: form.comment,
      })
      setForm((prev) => ({ ...prev, comment: '' }))
      await loadData()
    } catch {
      setError('Failed to submit feedback.')
    }
  }

  if (loading) return <div className="container"><div className="card">Loading...</div></div>

  return (
    <div className="container">
      <section className="stats-grid">
        <div className="stat-card"><h3>{courses.length}</h3><p>Total Courses</p></div>
        <div className="stat-card"><h3>{feedback.length}</h3><p>Feedback Entries</p></div>
      </section>

      <div className="two-col">
        {error && <section className="card full"><p className="error">{error}</p></section>}

        <section className="card">
          <h2>Courses</h2>
          <ul>
            {courses.map((course) => (
              <li key={course.id}>{course.code} - {course.title} ({course.instructor})</li>
            ))}
          </ul>
        </section>

        <section className="card">
          <h2>Submit Feedback</h2>
          <form onSubmit={submitFeedback}>
            <select value={form.courseId} onChange={(e) => setForm({ ...form, courseId: e.target.value })} required>
              {courses.map((course) => (
                <option key={course.id} value={course.id}>{course.code} - {course.title}</option>
              ))}
            </select>
            <input type="number" min="1" max="5" value={form.rating} onChange={(e) => setForm({ ...form, rating: e.target.value })} />
            <textarea value={form.comment} placeholder="Write your feedback" onChange={(e) => setForm({ ...form, comment: e.target.value })} />
            <button type="submit">Send Feedback</button>
          </form>
        </section>

        <section className="card full">
          <div className="feedback-header">
            <h2>All Feedback</h2>
            <input
              placeholder="Search by course, student, comment..."
              value={query}
              onChange={(e) => setQuery(e.target.value)}
            />
          </div>
          {filteredFeedback.length === 0 ? (
            <p>No feedback found.</p>
          ) : (
            <ul>
              {filteredFeedback.map((item) => (
                <li key={item.id}>
                  <strong>{item.courseCode} ({item.courseTitle})</strong> - {item.rating}/5 by {item.studentUsername}<br />
                  {item.comment}
                </li>
              ))}
            </ul>
          )}
        </section>
      </div>
    </div>
  )
}

export default DashboardPage
