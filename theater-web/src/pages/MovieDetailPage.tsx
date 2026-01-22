import { useEffect, useState } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { movieApi, showtimeApi } from '../api/client';
import Loading from '../components/Loading';
import type { Movie, Showtime } from '../types';

export default function MovieDetailPage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();
  const [movie, setMovie] = useState<Movie | null>(null);
  const [showtimes, setShowtimes] = useState<Showtime[]>([]);
  const [selectedDate, setSelectedDate] = useState<string>(() => {
    const today = new Date();
    return today.toISOString().split('T')[0];
  });
  const [loading, setLoading] = useState(true);

  const dates = Array.from({ length: 7 }, (_, i) => {
    const date = new Date();
    date.setDate(date.getDate() + i);
    return {
      value: date.toISOString().split('T')[0],
      label: date.toLocaleDateString('ko-KR', { month: 'short', day: 'numeric', weekday: 'short' }),
    };
  });

  useEffect(() => {
    const fetchMovie = async () => {
      try {
        const data = await movieApi.getById(Number(id));
        setMovie(data);
      } catch (error) {
        console.error('Failed to fetch movie:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchMovie();
  }, [id]);

  useEffect(() => {
    const fetchShowtimes = async () => {
      try {
        const data = await showtimeApi.getByMovie(Number(id), selectedDate);
        setShowtimes(data);
      } catch (error) {
        console.error('Failed to fetch showtimes:', error);
      }
    };

    if (id) fetchShowtimes();
  }, [id, selectedDate]);

  const handleShowtimeClick = (showtime: Showtime) => {
    navigate(`/booking/${showtime.id}`);
  };

  const formatTime = (dateString: string) => {
    return new Date(dateString).toLocaleTimeString('ko-KR', {
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const groupedShowtimes = showtimes.reduce(
    (acc, showtime) => {
      const key = showtime.theaterName;
      if (!acc[key]) acc[key] = [];
      acc[key].push(showtime);
      return acc;
    },
    {} as Record<string, Showtime[]>
  );

  if (loading) return <Loading />;
  if (!movie) return <div className="container" style={{ paddingTop: '100px' }}>영화를 찾을 수 없습니다.</div>;

  return (
    <div>
      <div
        className="hero"
        style={{
          backgroundImage: `url(${movie.posterUrl || 'https://via.placeholder.com/1920x500'})`,
        }}
      >
        <div className="container hero-content">
          <h1 className="hero-title">{movie.title}</h1>
          <div className="hero-meta">
            <span>{movie.rating}</span>
            <span>{movie.durationMinutes}분</span>
            <span>{movie.releaseDate}</span>
          </div>
          <p style={{ maxWidth: '600px', color: 'var(--text-secondary)' }}>{movie.description}</p>
        </div>
      </div>

      <section className="section">
        <div className="container">
          <h2 className="section-title">상영 시간표</h2>

          <div className="date-picker">
            {dates.map((date) => (
              <button
                key={date.value}
                className={`date-btn ${selectedDate === date.value ? 'selected' : ''}`}
                onClick={() => setSelectedDate(date.value)}
              >
                {date.label}
              </button>
            ))}
          </div>

          {Object.keys(groupedShowtimes).length > 0 ? (
            Object.entries(groupedShowtimes).map(([theater, times]) => (
              <div key={theater} style={{ marginBottom: '30px' }}>
                <h3 style={{ fontSize: '18px', marginBottom: '15px' }}>{theater}</h3>
                <div className="showtime-list">
                  {times.map((showtime) => (
                    <button
                      key={showtime.id}
                      className="showtime-btn"
                      onClick={() => handleShowtimeClick(showtime)}
                    >
                      <div>{formatTime(showtime.startTime)}</div>
                      <div style={{ fontSize: '12px', color: 'var(--text-secondary)' }}>
                        {showtime.screenName}
                      </div>
                    </button>
                  ))}
                </div>
              </div>
            ))
          ) : (
            <p style={{ color: 'var(--text-secondary)' }}>선택한 날짜에 상영 일정이 없습니다.</p>
          )}
        </div>
      </section>
    </div>
  );
}
