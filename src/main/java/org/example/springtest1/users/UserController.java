package org.example.springtest1.users;

import org.example.springtest1.Roles;
import org.example.springtest1.reservations.api.Reservation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.List;

@RestController
@RequestMapping("profile/")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping({"dashboard/", "dashboard"})
    public ResponseEntity<List<Reservation>> profileDashboard(
            @AuthenticationPrincipal UserEntity user,
            @RequestParam(name = "roomId", required = false) Long roomId,
            @RequestParam(name = "pageSize", required = false) Integer pageSize,
            @RequestParam(name = "pageNumber", required = false) Integer pageNumber
            ) {

        ReservationByUserSearchFilter filter = new ReservationByUserSearchFilter(roomId, pageSize, pageNumber);


        List<Reservation> reservations = userService.loadReservationByUser(user, filter);

        return ResponseEntity.status(HttpStatus.OK).body(reservations);
    }

    @GetMapping({"myRole", "myRole/"})
    public ResponseEntity<List<Roles>> myRole(@AuthenticationPrincipal UserEntity user) {
        var role = user.getRoles();
        return ResponseEntity.status(HttpStatus.OK).body(role);
    }

//    @GetMapping("addTestRole")
//    public ResponseEntity<String> addTestRole(
//            @AuthenticationPrincipal UserEntity user
//    ) {
//        userRolesService.addRoleToUser(user, Roles.ADMIN);
//        return ResponseEntity.status(HttpStatus.OK).body("Success");
//    }



}
