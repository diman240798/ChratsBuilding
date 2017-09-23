package ru.sfedu.nanotechnology.model;

import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("singleton")
public @Data class Settings {
}
