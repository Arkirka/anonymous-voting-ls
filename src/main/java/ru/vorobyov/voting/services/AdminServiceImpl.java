package ru.vorobyov.voting.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.vorobyov.voting.entities.Admin;
import ru.vorobyov.voting.repositories.AdminRepository;

@Service("adminService")
public class AdminServiceImpl implements AdminService{

    @Autowired
    public AdminService adminService;

    @Autowired
    private AdminRepository adminRepository;
    @Override
    public Iterable<Admin> findAll() {
        return adminRepository.findAll();
    }


}
