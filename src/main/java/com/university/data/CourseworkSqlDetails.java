package com.university.data;

import com.university.courses.*;
import com.university.courses.Assignment;
import com.university.courses.FinalExam;
import com.university.courses.MidtermExam;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * SQLite implementation of CourseworkDetails interface.
 * Demonstrates concrete implementation of persistence layer following the DIP principle.
 * Provides CRUD operations for coursework data using SQLite database.
 */