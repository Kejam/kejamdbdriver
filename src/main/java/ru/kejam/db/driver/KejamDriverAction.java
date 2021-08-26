package ru.kejam.db.driver;

import lombok.extern.slf4j.Slf4j;

import java.sql.DriverAction;

@Slf4j
public class KejamDriverAction implements DriverAction {
    @Override
    public void deregister() {
        log.warn("Deregister driver");
    }
}
