package org.sgomez.test.springboot.app.controllers;

import org.sgomez.test.springboot.app.dtos.TransactionReqDto;
import org.sgomez.test.springboot.app.models.Account;
import org.sgomez.test.springboot.app.services.IAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController()
@RequestMapping("/api/v1/accounts")
public class AccountController {
    @Autowired
    private IAccountService accountService;

    @GetMapping
    @ResponseStatus(OK)
    public List<Account> list() {
        return accountService.findAll();
    }
    @GetMapping("/{id}")
    @ResponseStatus(OK)
    public Account detail(@PathVariable Long id) {
        return accountService.findById(id);
    }
    @PostMapping
    @ResponseStatus(CREATED)
    public Account save(@RequestBody Account account) {
        return accountService.save(account);
    }
    @PostMapping("/transfer")
    public ResponseEntity<?> transfer(@RequestBody TransactionReqDto requestDto) {
        accountService.transfer(requestDto.getAccountOriginId(),
                requestDto.getAccountTargetId(),
                requestDto.getAmount(), requestDto.getBankId());

        Map<String, Object> response = new HashMap<>();
        response.put("date", LocalDate.now().toString());
        response.put("status", "OK");
        response.put("message", "Success");
        response.put("transaction", requestDto);

        return ResponseEntity.ok(response);
    }
}
