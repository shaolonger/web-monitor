import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { NzMessageService } from 'ng-zorro-antd/message';

import { AuthService } from '@app/service/auth.service';

@Component({
    selector: 'app-register',
    templateUrl: './register.component.html',
    styleUrls: ['./register.component.scss']
})
export class RegisterComponent implements OnInit {

    isLoading = false;
    validateForm!: FormGroup;

    constructor(
        private router: Router,
        private location: Location,
        private authService: AuthService,
        private message: NzMessageService,
        private fb: FormBuilder
    ) { }

    ngOnInit(): void {
        this.initFormData();
    }

    /**
     * 表单数据初始化
     */
    initFormData(): void {
        this.validateForm = this.fb.group({
            username: [null, [Validators.required]],
            password: [null, [Validators.required]],
            email: [null, [Validators.required]],
            phone: [''],
            gender: ['']
        });
    }

    /**
     * 返回
     */
    onBack(): void {
        this.location.back();
    }

    /**
     * 确认注册
     */
    onSubmit(): void {
        let params = this.validateForm.getRawValue();
        this.isLoading = true;
        this.authService.register(
            params,
            res => {
                console.log('[成功]注册', res);
                this.isLoading = false;
                if (res.success) {
                    console.log('[成功]注册', res);
                    this.message.success('注册成功，请等待管理员审核').onClose.subscribe(() => {
                        this.router.navigate(['/home']);
                    });
                } else {
                    console.log('[失败]注册', res);
                    this.message.error(res.msg || '注册失败');
                }
            }
        );
    }

}
