package com.makeFriends.dubbo.api;

import com.makeFriends.model.domain.Settings;

public interface SettingsApi {
    Settings findByUserId(Long userId);

    void save(Settings settings);

    void update(Settings settings);
}
