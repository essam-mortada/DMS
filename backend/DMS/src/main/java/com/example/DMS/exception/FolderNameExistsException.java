package com.example.DMS.exception;

public class FolderNameExistsException extends RuntimeException {
  public FolderNameExistsException(String message) {
    super(message);
  }
}
