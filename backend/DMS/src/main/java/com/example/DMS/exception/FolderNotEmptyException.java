package com.example.DMS.exception;

public class FolderNotEmptyException extends RuntimeException {
  public FolderNotEmptyException(String message) {
    super(message);
  }
}

