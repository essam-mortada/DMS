package com.example.DMS.DTO;

import com.example.DMS.models.SharePermission;
import lombok.Data;

@Data
public class CreateShareLinkRequest {
    private SharePermission permission;
}
