USE university_admission;
-- Add user_id column to admissions table to link it to the user who created it
ALTER TABLE admissions ADD COLUMN IF NOT EXISTS user_id INT;
-- Add unique constraint so one user has only one admission record (optional, but good for this flow)
ALTER TABLE admissions ADD UNIQUE (user_id);
