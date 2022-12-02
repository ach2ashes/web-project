package com.jee.faang;

import com.jee.faang.models.Role;
import com.jee.faang.models.metier.*;
import com.jee.faang.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static com.jee.faang.models.ERole.*;

@SpringBootApplication
public class SpringBootSecurityJwtApplication {
	@Autowired
	RoleRepository roleRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder encoder;

	@Autowired
	CompanyRepo companyRepo;

	@Autowired
	AdminRepo adminRepo;

	@Autowired
	GuestRepo guestRepo;

	@Autowired
	EmployeeRepo employeeRepo;

	@Autowired
	EnrollmentRepo enrollmentRepo;




	public static void main(String[] args) {
    SpringApplication.run(SpringBootSecurityJwtApplication.class, args);
	}
	@Bean
	CommandLineRunner commandLineRunner() {
		return (args) -> {
			roleRepository.save(new Role(ROLE_ADMIN));
			roleRepository.save(new Role(ROLE_GUEST));
			roleRepository.save(new Role(ROLE_EMPLOYEE));
			Admin user1 = new Admin("nouhaila-benlguarni@gmail.com","nouhaila-benlguarni@gmail.com", encoder.encode("password123"),"Nouhaila Benlguarni");
			Set<Role> roles = new HashSet<>();
			roles.add(roleRepository.findByName(ROLE_ADMIN).orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
			user1.setRoles(roles);
			adminRepo.save(user1);

			Employee employee1 = new Employee("hanae-alaoui@gmail.com","hanae-alaoui@gmail.com",encoder.encode("password123"),"Hanae Alaoui");
			Set<Role> roles2 = new HashSet<>();
			roles2.add(roleRepository.findByName(ROLE_EMPLOYEE).orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
			employee1.setRoles(roles2);
			employeeRepo.save(employee1);

			Guest guest1 = new Guest("mouad-benlguarni@gmail.com","mouad-benlguarni@gmail.com",encoder.encode("password123"),"mouad benlguarni");
			Set<Role> roles3 = new HashSet<>();
			roles3.add(roleRepository.findByName(ROLE_GUEST).orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
			guest1.setRoles(roles3);
			guestRepo.save(guest1);

			Employee employee2 = new Employee("soussi-alaoui@gmail.com","soussi-alaoui@gmail.com",encoder.encode("password123"),"soussi Alaoui");
			Set<Role> roles4 = new HashSet<>();
			roles4.add(roleRepository.findByName(ROLE_EMPLOYEE).orElseThrow(() -> new RuntimeException("Error: Role is not found.")));
			employee2.setRoles(roles4);
			employeeRepo.save(employee2);

			Company company1 = new Company(null,"JAVA 1/2","Cours de programmation Java pour débutants", LocalDate.of(2022,10,10),LocalDate.of(2022,11,22),null,employee1);
			Company company2 = new Company(null,"SQL 1/2","Découvrez les compétences SQL essentielles ",LocalDate.of(2022,10,11),LocalDate.of(2022,11,23),null,employee1);
			Company company11 = new Company(null,"JAVA2/2","Cours de programmation Java pour débutants (suite)", LocalDate.of(2022,10,07),LocalDate.of(2022,11,17),null,employee1);
			Company company22 = new Company(null,"SQL2/2","Découvrez les compétences SQL essentielles (suite)",LocalDate.of(2022,10,11),LocalDate.of(2022,11,15),null,employee1);
			Company company3 = new Company(null,"Python","Ce cours Python est pour les débutants",LocalDate.of(2022,10,12),LocalDate.of(2022,11,19),null,employee2);
			Company company4 = new Company(null,"Data Science","Développez des compétences en science des données",LocalDate.of(2022,10,13),LocalDate.of(2022,11,18),null,employee2);
			companyRepo.save(company1);
			companyRepo.save(company2);
			companyRepo.save(company11);
			companyRepo.save(company22);
			companyRepo.save(company3);
			companyRepo.save(company4);
			employee1.setCompanies(Arrays.asList(company1,company2,company3,company4));
			userRepository.save(employee1);
			Enrollment enrollment = new Enrollment(null,guest1,company1,LocalDate.of(2022,10,15));
			enrollmentRepo.save(enrollment);
		};
	};


}
