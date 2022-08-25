package ru.magnit.magreportbackend.domain;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serial;

@Getter
@Setter
@ToString(onlyExplicitlyIncluded = true, callSuper = true)
@Accessors(chain = true)
@NoArgsConstructor
@MappedSuperclass
@EqualsAndHashCode(onlyExplicitlyIncluded = true, callSuper = true)
@Schema(name = "Базовая сущность для всех сущностей, имеющих имя и описание")
public abstract class EntityWithName extends BaseEntity {

    @Serial
    private static final long serialVersionUID = 1L;

    @ToString.Include
    @Column(name = "NAME")
    @Schema(name = "Имя")
    protected String name;

    @ToString.Include
    @Column(name = "DESCRIPTION")
    @Schema(name = "Описание")
    protected String description;
}
