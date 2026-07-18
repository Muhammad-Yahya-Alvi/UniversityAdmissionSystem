USE university_admission;

CREATE TABLE IF NOT EXISTS users (
    id INT AUTO_INCREMENT PRIMARY KEY,
    full_name VARCHAR(100),
    email VARCHAR(100) UNIQUE,
    password VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Keep the existing admissions table
CREATE TABLE IF NOT EXISTS admissions (
    id INT AUTO_INCREMENT PRIMARY KEY,
    student_name VARCHAR(100),
    father_name VARCHAR(100),
    cnic VARCHAR(20),
    course VARCHAR(100),
    phone VARCHAR(20),
    admission_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
