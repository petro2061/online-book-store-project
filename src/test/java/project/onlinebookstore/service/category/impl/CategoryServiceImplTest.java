package project.onlinebookstore.service.category.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import project.onlinebookstore.dto.category.CategoryDto;
import project.onlinebookstore.dto.category.CreateCategoryRequestDto;
import project.onlinebookstore.exception.EntityNotFoundException;
import project.onlinebookstore.mapper.CategoryMapper;
import project.onlinebookstore.model.Category;
import project.onlinebookstore.repository.category.CategoryRepository;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {
    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private CategoryMapper categoryMapper;

    @InjectMocks
    private CategoryServiceImpl categoryServiceImpl;

    @Test
    @DisplayName("Return all categories")
    void getAllCategory_withPageable_shouldReturnAllCategory() {
        //Given
        Pageable pageable = PageRequest.of(0, 10);
        List<Category> categoryList = getCategoryList();
        List<CategoryDto> categoryDtoList = getCategoryDtoList();

        when(categoryRepository.findAll(pageable)).thenReturn(new PageImpl<>(categoryList));
        when(categoryMapper.toCategoryDto(ArgumentMatchers.any(Category.class)))
                .thenReturn(categoryDtoList.get(0));

        //When
        List<CategoryDto> actualAllCategoryDtoList = categoryServiceImpl.findAll(pageable);

        //Then
        assertEquals(categoryDtoList, actualAllCategoryDtoList);

        verify(categoryRepository).findAll(pageable);
        verify(categoryMapper, times(categoryList.size()))
                .toCategoryDto(categoryList.get(0));
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Find category by valid id")
    void getCategoryById_withValidId_shouldReturnValidCategoryDto() {
        //Given
        Long categoryId = 1L;
        Category category = getCategory(getCreateCategoryRequestDto());
        CategoryDto categoryDto = getCategoryDto(category);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryDto);

        //When
        CategoryDto actualCategoryDto = categoryServiceImpl.findById(categoryId);

        //Then
        assertEquals(categoryDto, actualCategoryDto);

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).toCategoryDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Find category by not valid id")
    void getCategoryById_withNotValidId_shouldThrowException() {
        //Given
        Long notValidCategoryId = 1000L;

        when(categoryRepository.findById(notValidCategoryId))
                .thenReturn(Optional.empty());

        //When
        assertThrows(EntityNotFoundException.class,
                () -> categoryServiceImpl.findById(notValidCategoryId));

        verify(categoryRepository).findById(notValidCategoryId);
        verify(categoryMapper, never()).toCategoryDto(ArgumentMatchers.any());
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Save category with valid CategoryRequestDto")
    void getSaveCategory_withValidCategoryRequestDto_shouldReturnSaveCategoryDto() {
        //Given
        CreateCategoryRequestDto createCategoryRequestDto = getCreateCategoryRequestDto();
        Category category = getCategory(createCategoryRequestDto);
        CategoryDto categoryDto = getCategoryDto(category);

        when(categoryMapper.toCategoryModel(createCategoryRequestDto)).thenReturn(category);
        when(categoryRepository.save(category)).thenReturn(category);
        when(categoryMapper.toCategoryDto(category)).thenReturn(categoryDto);

        //When
        CategoryDto actualCategoryDto = categoryServiceImpl.save(createCategoryRequestDto);

        //Then
        assertEquals(categoryDto, actualCategoryDto);

        verify(categoryMapper).toCategoryModel(createCategoryRequestDto);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toCategoryDto(category);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Update category by valid id and valid CategoryRequestDto")
    void updateCategory_withCategoryIdAndCategoryRequestDto_shouldValidUpdateCategoryDto() {
        //Given
        Long categoryId = 1L;
        CreateCategoryRequestDto updateCategoryRequestDto = getUpdateCategoryRequestDto();
        Category category = getCategory(getCreateCategoryRequestDto());
        Category updateCategory = getCategory(updateCategoryRequestDto);
        CategoryDto categoryDto = getCategoryDto(getCategory(updateCategoryRequestDto));

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        doNothing().when(categoryMapper)
                .updateCategoryFromDto(updateCategoryRequestDto, category);
        when(categoryRepository.save(category)).thenReturn(updateCategory);
        when(categoryMapper.toCategoryDto(updateCategory)).thenReturn(categoryDto);

        //When
        CategoryDto actualUpdateCategory =
                categoryServiceImpl.update(categoryId, updateCategoryRequestDto);

        //Then
        assertEquals(categoryDto, actualUpdateCategory);

        verify(categoryRepository).findById(categoryId);
        verify(categoryMapper).updateCategoryFromDto(updateCategoryRequestDto, category);
        verify(categoryRepository).save(category);
        verify(categoryMapper).toCategoryDto(updateCategory);
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Update category by not valid id")
    void updateCategory_withNotValidId_shouldThrowException() {
        //Given
        Long notValidCategoryId = 1000L;
        CreateCategoryRequestDto updateCategoryRequestDto = getUpdateCategoryRequestDto();

        //When
        when(categoryRepository.findById(notValidCategoryId)).thenReturn(Optional.empty());

        //Then
        assertThrows(EntityNotFoundException.class,
                () -> categoryServiceImpl.update(notValidCategoryId, updateCategoryRequestDto));

        verify(categoryRepository).findById(notValidCategoryId);
        verify(categoryMapper, never())
                .updateCategoryFromDto(ArgumentMatchers.any(), ArgumentMatchers.any());
        verifyNoMoreInteractions(categoryRepository, categoryMapper);
    }

    @Test
    @DisplayName("Delete category by id")
    void deleteCategoryById_withValidCategoryId_shouldDeleteCategory() {
        //Given
        Long categoryId = 1L;

        //When
        categoryServiceImpl.deleteById(categoryId);

        verify(categoryRepository).deleteById(categoryId);
        verifyNoMoreInteractions(categoryRepository);
    }

    private List<Category> getCategoryList() {
        Category category = new Category();
        category.setId(1L);
        category.setName("Sample Name");
        category.setDescription("Sample Description");
        return List.of(category);
    }

    private List<CategoryDto> getCategoryDtoList() {
        CategoryDto categoryDto = new CategoryDto(
                1L,
                "Sample Name",
                "Sample Description"
        );
        return List.of(categoryDto);
    }

    private CreateCategoryRequestDto getCreateCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Sample name",
                "Sample Description"
        );
    }

    private Category getCategory(CreateCategoryRequestDto categoryRequestDto) {
        Category category = new Category();
        category.setId(1L);
        category.setName(getCreateCategoryRequestDto().name());
        category.setDescription(categoryRequestDto.description());

        return category;
    }

    private CategoryDto getCategoryDto(Category category) {
        return new CategoryDto(
                category.getId(),
                category.getName(),
                category.getDescription()
        );
    }

    private CreateCategoryRequestDto getUpdateCategoryRequestDto() {
        return new CreateCategoryRequestDto(
                "Sample name 1",
                "Sample Description 1"
        );
    }
}
