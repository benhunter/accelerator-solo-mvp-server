package com.benhunter.solomvpserver;

import org.springframework.data.repository.CrudRepository;
//import org.springframework.security.access.annotation.Secured;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin("http://localhost:3000")
@CrossOrigin()
//@PreAuthorize("hasRole('USER')")
//@Secured("USER")
public interface AlarmRepository extends CrudRepository<Alarm, Long> {
}
