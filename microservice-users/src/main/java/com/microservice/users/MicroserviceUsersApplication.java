package com.microservice.users;

import com.microservice.users.persistence.entity.PermissionEntity;
import com.microservice.users.persistence.entity.RoleEntity;
import com.microservice.users.persistence.entity.RoleEnum;
import com.microservice.users.persistence.entity.UserEntity;
import com.microservice.users.persistence.entity.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Set;

@EnableDiscoveryClient
@SpringBootApplication
public class MicroserviceUsersApplication {

	public static void main(String[] args) {
		SpringApplication.run(MicroserviceUsersApplication.class, args);
	}

	@Bean
	CommandLineRunner init(UserRepository userRepository) {
		return args -> {
			// 🔑 Permisos genéricos
			PermissionEntity createPermission = PermissionEntity.builder().name("CREATE").build();
			PermissionEntity readPermission   = PermissionEntity.builder().name("READ").build();
			PermissionEntity updatePermission = PermissionEntity.builder().name("UPDATE").build();
			PermissionEntity deletePermission = PermissionEntity.builder().name("DELETE").build();

			// 👑 Rol ADMIN → todo
			RoleEntity roleAdmin = RoleEntity.builder()
					.roleEnum(RoleEnum.ADMIN)
					.permissionList(Set.of(createPermission, readPermission, updatePermission, deletePermission))
					.build();

			// 🧑‍⚕️ Rol DOCTOR → leer y actualizar (sus citas, historial pacientes)
			RoleEntity roleDoctor = RoleEntity.builder()
					.roleEnum(RoleEnum.DOCTOR)
					.permissionList(Set.of(readPermission, updatePermission))
					.build();

			// 👤 Rol PACIENTE → crear y leer (sus citas)
			RoleEntity rolePaciente = RoleEntity.builder()
					.roleEnum(RoleEnum.PACIENTE)
					.permissionList(Set.of(createPermission, readPermission))
					.build();

			// 📝 Rol RECEPCIONISTA → crear, leer y actualizar (citas de pacientes)
			RoleEntity roleRecepcionista = RoleEntity.builder()
					.roleEnum(RoleEnum.RECEPCIONISTA)
					.permissionList(Set.of(createPermission, readPermission, updatePermission))
					.build();

			// 👥 Usuarios de prueba
			UserEntity userbrayan = UserEntity.builder()
					.username("Brayan")
					.password("$2a$10$UoKlw/qmes2W.KEjAsaIAeNFo4mhzBZHH9EHlV6Wuhk2RCOS/pOWC")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleAdmin))
					.build();

			UserEntity userstiven = UserEntity.builder()
					.username("Stiven")
					.password("$2a$10$UoKlw/qmes2W.KEjAsaIAeNFo4mhzBZHH9EHlV6Wuhk2RCOS/pOWC")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleDoctor))
					.build();

			UserEntity userdavid = UserEntity.builder()
					.username("David")
					.password("$2a$10$UoKlw/qmes2W.KEjAsaIAeNFo4mhzBZHH9EHlV6Wuhk2RCOS/pOWC")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(rolePaciente))
					.build();

			UserEntity userjuan = UserEntity.builder()
					.username("Juan")
					.password("$2a$10$UoKlw/qmes2W.KEjAsaIAeNFo4mhzBZHH9EHlV6Wuhk2RCOS/pOWC")
					.isEnabled(true)
					.accountNoExpired(true)
					.accountNoLocked(true)
					.credentialNoExpired(true)
					.roles(Set.of(roleRecepcionista))
					.build();

			// 💾 Guardar en la base de datos
			userRepository.saveAll(List.of(userbrayan, userstiven, userdavid, userjuan));

			System.out.println("Usuarios de prueba creados ✅");
		};
	}
}
