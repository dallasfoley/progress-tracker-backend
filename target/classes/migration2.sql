ALTER TABLE books
  ADD COLUMN ratings_count INTEGER DEFAULT 0;

UPDATE books
SET ratings_count = 0, rating = 0;