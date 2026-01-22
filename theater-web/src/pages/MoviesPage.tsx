import { useEffect, useState } from 'react';
import { movieApi } from '../api/client';
import MovieCard from '../components/MovieCard';
import Loading from '../components/Loading';
import type { Movie } from '../types';

export default function MoviesPage() {
  const [movies, setMovies] = useState<Movie[]>([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState<'all' | 'showing' | 'upcoming'>('all');
  const [searchQuery, setSearchQuery] = useState('');

  useEffect(() => {
    const fetchMovies = async () => {
      try {
        const data = await movieApi.getAll();
        setMovies(data);
      } catch (error) {
        console.error('Failed to fetch movies:', error);
      } finally {
        setLoading(false);
      }
    };

    fetchMovies();
  }, []);

  const handleSearch = async () => {
    if (!searchQuery.trim()) {
      const data = await movieApi.getAll();
      setMovies(data);
      return;
    }

    try {
      const data = await movieApi.search(searchQuery);
      setMovies(data);
    } catch (error) {
      console.error('Search failed:', error);
    }
  };

  const filteredMovies = movies.filter((movie) => {
    if (filter === 'all') return true;
    return movie.status === filter;
  });

  if (loading) return <Loading />;

  return (
    <div style={{ paddingTop: '80px' }}>
      <section className="section">
        <div className="container">
          <h2 className="section-title">영화</h2>

          {/* Search & Filter */}
          <div style={{ display: 'flex', gap: '20px', marginBottom: '30px', flexWrap: 'wrap' }}>
            <div style={{ display: 'flex', gap: '10px' }}>
              <input
                type="text"
                placeholder="영화 검색..."
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                onKeyDown={(e) => e.key === 'Enter' && handleSearch()}
                style={{
                  padding: '10px 15px',
                  background: 'var(--bg-card)',
                  border: '1px solid var(--border-color)',
                  borderRadius: '4px',
                  color: 'var(--text-primary)',
                  width: '250px',
                }}
              />
              <button className="btn btn-primary" onClick={handleSearch}>
                검색
              </button>
            </div>

            <div style={{ display: 'flex', gap: '10px' }}>
              {[
                { value: 'all', label: '전체' },
                { value: 'showing', label: '상영중' },
                { value: 'upcoming', label: '개봉예정' },
              ].map((option) => (
                <button
                  key={option.value}
                  className={`btn ${filter === option.value ? 'btn-primary' : 'btn-secondary'}`}
                  onClick={() => setFilter(option.value as 'all' | 'showing' | 'upcoming')}
                >
                  {option.label}
                </button>
              ))}
            </div>
          </div>

          {/* Movie Grid */}
          {filteredMovies.length > 0 ? (
            <div className="movie-grid">
              {filteredMovies.map((movie) => (
                <MovieCard key={movie.id} movie={movie} />
              ))}
            </div>
          ) : (
            <p style={{ color: 'var(--text-secondary)' }}>영화가 없습니다.</p>
          )}
        </div>
      </section>
    </div>
  );
}
