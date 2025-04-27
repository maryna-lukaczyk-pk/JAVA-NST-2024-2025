package org.example.projectmanager.service;

import org.example.projectmanager.dto.user.UserCreateDto;
import org.example.projectmanager.dto.user.UserDto;
import org.example.projectmanager.dto.user.UserEditDto;
import org.example.projectmanager.entity.user.User;
import org.example.projectmanager.exception.EntityNotFoundException;
import org.example.projectmanager.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService implements IUserService {
    private final UserRepository repository;

    public UserService(final UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<UserDto> findAll() {
        return this.repository.findAll().stream().map(UserDto::MapFromEntity).toList();
    }

    @Override
    public Long create(UserCreateDto dto) {
        final User user = dto.MapToEntity();
        var created = this.repository.save(user);

        return created.getId();
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {
        if(this.repository.existsById(id)) {
            this.repository.deleteById(id);
        }

        throw createEntityNotFoundException(id);
    }

    @Override
    public void edit(UserEditDto dto) throws EntityNotFoundException {
        var user = findByIdOrThrow(dto.id);
        dto.UpdateEntity(user);
        this.repository.flush();
    }

    @Override
    public UserDto getById(Long id) throws EntityNotFoundException {
        var user = findByIdOrThrow(id);
        return UserDto.MapFromEntity(user);
    }

    private User findByIdOrThrow(Long id) throws EntityNotFoundException {
        return this.repository.findById(id).orElseThrow(() -> createEntityNotFoundException(id));
    }

    private static EntityNotFoundException createEntityNotFoundException(Long id) {
        return new EntityNotFoundException(String.format("User by the id %d has not been found", id));
    }
}
