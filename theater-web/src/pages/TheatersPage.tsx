import { useEffect, useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { theaterApi, showtimeApi } from '../api/client';
import Loading from '../components/Loading';
import type { Theater, Showtime } from '../types';

export default function TheatersPage() {
  const navigate = useNavigate();
  const [theaters, setTheaters] = useState<Theater[]>([]);
  const [selectedTheater, setSelectedTheater] = useState<Theater | null>(null);
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
    const fetchTheaters = async () => {
      try {
        const data = await theaterApi.getAll();
        setTheaters(data);
        if (data.length > 0) {
          setSelectedTheater(data[0]);
        }
      } catch (error) {
        console.error('Failed to fetch theaters:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchTheaters();
  }, []);

  useEffect(() => {
    const fetchShowtimes = async () => {
      if (!selectedTheater) return;

      try {
        const data = await showtimeApi.getByTheater(selectedTheater.id, selectedDate);
        setShowtimes(data);
      } catch (error) {
        console.error('Failed to fetch showtimes:', error);
      }
    };

    fetchShowtimes();
  }, [selectedTheater, selectedDate]);

  const formatTime = (dateString: string) => {
    return new Date(dateString).toLocaleTimeString('ko-KR', {
      hour: '2-digit',
      minute: '2-digit',
    });
  };

  const groupedShowtimes = showtimes.reduce(
    (acc, showtime) => {
      const key = showtime.movieTitle;
      if (!acc[key]) acc[key] = [];
      acc[key].push(showtime);
      return acc;
    },
    {} as Record<string, Showtime[]>
  );

  if (loading) return <Loading />;

  return (
    <div style={{ paddingTop: '80px' }}>
      <section className="section">
        <div className="container">
          <h2 className="section-title">극장</h2>

          <div style={{ display: 'grid', gridTemplateColumns: '250px 1fr', gap: '40px' }}>
            {/* Theater List */}
            <div>
              <h3 style={{ fontSize: '16px', marginBottom: '15px', color: 'var(--text-secondary)' }}>
                극장 선택
              </h3>
              {theaters.map((theater) => (
                <div
                  key={theater.id}
                  onClick={() => setSelectedTheater(theater)}
                  style={{
                    padding: '15px',
                    background: selectedTheater?.id === theater.id ? 'var(--bg-hover)' : 'transparent',
                    borderRadius: '4px',
                    cursor: 'pointer',
                    marginBottom: '5px',
                    borderLeft: selectedTheater?.id === theater.id ? '3px solid var(--primary)' : '3px solid transparent',
                  }}
                >
                  <div style={{ fontWeight: 600 }}>{theater.name}</div>
                  <div style={{ fontSize: '13px', color: 'var(--text-secondary)' }}>
                    {theater.location}
                  </div>
                </div>
              ))}
            </div>

            {/* Showtimes */}
            <div>
              {selectedTheater && (
                <>
                  <div style={{ marginBottom: '20px' }}>
                    <h3 style={{ fontSize: '20px', marginBottom: '5px' }}>{selectedTheater.name}</h3>
                    <p style={{ color: 'var(--text-secondary)', fontSize: '14px' }}>
                      {selectedTheater.address}
                    </p>
                  </div>

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
                    Object.entries(groupedShowtimes).map(([movie, times]) => (
                      <div
                        key={movie}
                        style={{
                          background: 'var(--bg-card)',
                          borderRadius: '8px',
                          padding: '20px',
                          marginBottom: '15px',
                        }}
                      >
                        <h4 style={{ marginBottom: '15px' }}>{movie}</h4>
                        <div className="showtime-list">
                          {times.map((showtime) => (
                            <button
                              key={showtime.id}
                              className="showtime-btn"
                              onClick={() => navigate(`/booking/${showtime.id}`)}
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
                    <p style={{ color: 'var(--text-secondary)' }}>
                      선택한 날짜에 상영 일정이 없습니다.
                    </p>
                  )}
                </>
              )}
            </div>
          </div>
        </div>
      </section>
    </div>
  );
}
