package service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.github.viblanc.profilemanager.dto.UserTypeDto;
import com.github.viblanc.profilemanager.entity.UserType;
import com.github.viblanc.profilemanager.mappers.UserTypeMapper;
import com.github.viblanc.profilemanager.repository.UserTypeRepository;
import com.github.viblanc.profilemanager.service.UserTypeServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceTest {

	@InjectMocks
	private UserTypeServiceImpl userTypeService;
	
	@Spy
	private UserTypeMapper mapper = Mappers.getMapper(UserTypeMapper.class);
	
	@Mock
	private UserTypeRepository userTypeRepository;
	
	private static final UserType USER_TYPE = new UserType(1L, "Admin", null);
	private static final UserTypeDto USER_TYPE_DTO = new UserTypeDto(1L, "Admin");
	
	@Test
	void testGetAllUserTypes() {
		when(userTypeRepository.findAll()).thenReturn(List.of(new UserType(1L, "Admin", null), new UserType(2L, "User", null)));
		
		List<UserTypeDto> expected = List.of(new UserTypeDto(1L, "Admin"), new UserTypeDto(2L, "User"));
		List<UserTypeDto> actual = userTypeService.findAll();
		
		assertEquals(expected, actual);
	}
	
	@Test
	void testGetUserType() {
		when(userTypeRepository.findById(USER_TYPE.getId())).thenReturn(Optional.of(USER_TYPE));
		
		UserTypeDto actual = userTypeService.getUserType(USER_TYPE_DTO.id());
		
		assertEquals(USER_TYPE_DTO, actual);
	}
	
	@Test
	void testAddUserType() {
		when(userTypeRepository.save(any(UserType.class))).thenReturn(USER_TYPE);
		
		UserTypeDto actual = userTypeService.addUserType(USER_TYPE_DTO);
		
		assertEquals(USER_TYPE_DTO, actual);
	}
	
	@Test
	void testUpdateUserType() {
		when(userTypeRepository.findById(USER_TYPE.getId())).thenReturn(Optional.of(USER_TYPE));
		when(userTypeRepository.save(any(UserType.class))).thenReturn(USER_TYPE);
		
		UserTypeDto actual = userTypeService.editUserType(USER_TYPE.getId(), USER_TYPE_DTO);
		
		assertEquals(USER_TYPE_DTO, actual);
	}
	
	@Test
	void testDeleteUserType() {
		userTypeService.deleteUserType(USER_TYPE.getId());
		
		verify(userTypeRepository).deleteById(USER_TYPE.getId());
	}
	
}
