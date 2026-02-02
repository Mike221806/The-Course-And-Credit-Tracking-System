package com.university.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.university.roles.Advisor;
import com.university.data.AdvisorFeedbackDetails;
import com.university.data.AdvisorFeedbackSqlDetails;
import com.university.data.StudentDetails;
import com.university.data.StudentSqlDetails;
import com.university.roles.Student;

import java.util.List;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller for adding advisor feedback.
 * Allows advisors to add notes and feedback for students.
 */
