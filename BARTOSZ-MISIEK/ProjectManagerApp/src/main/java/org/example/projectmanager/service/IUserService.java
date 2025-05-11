package org.example.projectmanager.service;

import org.example.projectmanager.dto.user.UserCreateDto;
import org.example.projectmanager.dto.user.UserDto;
import org.example.projectmanager.dto.user.UserEditDto;
import org.example.projectmanager.exception.EntityNotFoundException;

import java.util.List;

public interface IUserService {
    /**
     * Returns the list of all entities
     */
    List<UserDto> findAll();

    /**
     * Creates an entity based on the provided dto object
     *
     * @return Returns the id of the created entity
     */
    Long create(UserCreateDto dto);

    void delete(Long id) throws EntityNotFoundException;
    void edit(UserEditDto dto) throws EntityNotFoundException;
    UserDto getById(Long id) throws EntityNotFoundException;
}
