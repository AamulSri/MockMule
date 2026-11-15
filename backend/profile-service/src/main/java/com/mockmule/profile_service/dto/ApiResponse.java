package com.mockmule.profile_service.dto;

import java.time.LocalDateTime;
import java.util.*;

public record ApiResponse<T>(
     LocalDateTime timeStamp,
     int status,
     String message,
     T data,
     Map<String,String> errors

) {}
