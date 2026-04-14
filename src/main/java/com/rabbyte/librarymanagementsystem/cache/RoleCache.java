package com.rabbyte.librarymanagementsystem.cache;

import com.rabbyte.librarymanagementsystem.entities.Role;
import com.rabbyte.librarymanagementsystem.repositories.RoleRepository;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Slf4j
@RequiredArgsConstructor
public class RoleCache {

    private final RoleRepository roleRepository;

    // Map<roleName, Role> - dùng ConcurrentHashMap để an toàn đa luồng
    @Getter
    private final Map<String, Role> roles = new ConcurrentHashMap<>();

    /**
     * Load tất cả roles vào cache ngay khi Spring khởi tạo bean này
     */
    @PostConstruct
    public void init() {
        roleRepository.findAll().forEach(role ->
                roles.put(role.getRoleName().name().toUpperCase(), role));

        log.info("Success to fetch {} roles into cache: {}", roles.size(), roles.keySet());
    }

    public Role getRoleByName(String roleName) {
        if (roleName == null) return null;
        return roles.get(roleName.trim().toUpperCase());
    }

    /**
     * Reload cache khi cần (ví dụ: Admin thêm role mới)
     */
    public void reload() {
        roles.clear();
        init();
    }
}
