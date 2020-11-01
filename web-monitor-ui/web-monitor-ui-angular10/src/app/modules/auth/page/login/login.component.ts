import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';

import { AuthService } from '@app/service/auth.service';
import { UserService } from '@data/service/user.service';

@Component({
    selector: 'app-login',
    templateUrl: './login.component.html',
    styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

    isLoading = false;
    validateForm!: FormGroup;

    constructor(
        private router: Router,
        private authService: AuthService,
        private message: NzMessageService,
        private userService: UserService,
        private fb: FormBuilder
    ) { }

    ngOnInit(): void {
        this.initLoginInfo();
    }

    /**
     * 从本地缓存中恢复用户名、密码
     */
    initLoginInfo(): void {
        let loginInfo = null;
        let storage = window.localStorage.getItem('ump-loginInfo');
        if (storage) {
            loginInfo = JSON.parse(storage);
        }
        this.validateForm = this.fb.group({
            username: [loginInfo?.username, [Validators.required]],
            password: [loginInfo?.password, [Validators.required]],
            remember: [Boolean(loginInfo?.remember)]
        });
    }

    submitForm(): void {
        // for (const i in this.validateForm.controls) {
        //     this.validateForm.controls[i].markAsDirty();
        //     this.validateForm.controls[i].updateValueAndValidity();
        // }
        let { username, password, remember } = this.validateForm.getRawValue();
        this.isLoading = true;
        this.authService.login(
            { username, password },
            res => {
                console.log('[成功]登录', res);
                this.isLoading = false;
                const { success, data, msg } = res;
                if (success) {
                    // 将用户信息存入state及本地
                    this.userService.setUserInfo({ ...data, hasLogin: true });
                    this.authService.setToken(data.token);
                    window.sessionStorage.setItem('ump-userInfo', JSON.stringify(data));
                    // 若选择了记住密码，则将用户名、密码存入本地
                    if (remember) {
                        window.localStorage.setItem('ump-loginInfo', JSON.stringify({
                            username, password, remember
                        }));
                    } else {
                        window.localStorage.removeItem('ump-loginInfo');
                    }
                    this.message.success('登录成功，即将跳转', {
                        nzDuration: 1500
                    }).onClose.subscribe(() => {
                        this.router.navigate(['/home']);
                    });
                } else {
                    this.message.error(msg || '登录失败');
                }
            }
        );
    }

}
