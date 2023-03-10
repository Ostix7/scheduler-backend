package ua.edu.ukma.mandarin.scheduler.service;

import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import ua.edu.ukma.mandarin.scheduler.domain.dto.GroupDTO;
import ua.edu.ukma.mandarin.scheduler.domain.entity.Group;
import ua.edu.ukma.mandarin.scheduler.domain.entity.Student;
import ua.edu.ukma.mandarin.scheduler.domain.entity.Subject;
import ua.edu.ukma.mandarin.scheduler.domain.entity.Teacher;
import ua.edu.ukma.mandarin.scheduler.repository.GroupRepository;

@Service
@RequiredArgsConstructor
public class GroupService {

    private final GroupRepository groupRepository;
    private final TeacherService teacherService;
    private final StudentService studentService;

    public void addGroupsForSubject(List<GroupDTO> groupDTOList, Subject subject) {
        List<Group> groupsToSave =
                groupDTOList.stream()
                        .map(
                                groupDTO ->
                                        Group.builder()
                                                .id(groupDTO.getId())
                                                .subject(subject)
                                                .number(groupDTO.getNumber())
                                                .teacher(getTeacher(groupDTO.getTeacherId()))
                                                .build())
                        .collect(Collectors.toList());

        groupRepository.saveAll(groupsToSave);
    }

    public Teacher getTeacher(Long id) {
        return teacherService.getTeacher(id);
    }

    public Student getStudent(Long id) {
        return studentService.getStudentById(id);
    }

    public List<GroupDTO> findAllGroupsForTeacherId(Long teacherId) {
        return groupRepository.findAllByTeacherId(teacherId).stream()
                .map(group -> new GroupDTO(group.getId(), group.getNumber(), group.getTeacher().getId()))
                .collect(Collectors.toList());
    }

    public void deleteGroupById(Long id) {
        groupRepository.findById(id).ifPresent(group -> groupRepository.deleteById(id));
    }

    public void registerToGroup(Long studentId, Long groupId) {
        groupRepository
                .findById(groupId)
                .ifPresent(
                        group -> {
                            group.getStudents().add(studentService.getStudentById(studentId));
                            groupRepository.save(group);
                        });
    }

    public void unregisterFromGroup(Long studentId, Long groupId) {
        groupRepository
                .findById(groupId)
                .ifPresent(
                        group -> {
                            group.getStudents().remove(studentService.getStudentById(studentId));
                            groupRepository.save(group);
                        });
    }

    @Transactional
    public void updateGroupsForSubject(List<GroupDTO> groupDTOList, Subject subjectSaved) {
        //TODO for later: Should implement better update for group
        deleteAllGroupsForSubject(subjectSaved);
        List<Group> groupsToSave =
                groupDTOList.stream()
                        .map(
                                groupDTO ->
                                        Group.builder()
                                                .id(groupDTO.getId())
                                                .subject(subjectSaved)
                                                .number(groupDTO.getNumber())
                                                .teacher(getTeacher(groupDTO.getTeacherId()))
                                                .build())
                        .collect(Collectors.toList());

        groupRepository.saveAll(groupsToSave);

    }

    @Transactional
    public void deleteAllGroupsForSubject(Subject subjectSaved) {
        groupRepository.deleteAllBySubject(subjectSaved);
    }
}
