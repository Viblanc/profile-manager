import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { UserTypeApi } from '../../user-type/user-type-api';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserApi } from '../user-api';
import { notBlank } from '../../shared/custom-validators';
import { Router } from '@angular/router';

@Component({
  selector: 'app-new-user',
  imports: [ReactiveFormsModule],
  templateUrl: './new-user.html',
  styleUrl: './new-user.css',
})
export class NewUser implements OnInit {
  private userApi = inject(UserApi);
  private userTypeApi = inject(UserTypeApi);
  private destroyRef = inject(DestroyRef);
  private formBuilder = inject(FormBuilder);
  private router = inject(Router);
  userTypes = signal<UserType[]>([]);
  form = this.formBuilder.nonNullable.group({
    lastName: ['', [Validators.required, notBlank]],
    firstName: ['', [Validators.required, notBlank]],
    email: ['', [Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]],
    userType: ['', [Validators.required]],
  });

  ngOnInit(): void {
    const subscription = this.userTypeApi.getUserTypes().subscribe({
      next: (userTypes) => {
        this.userTypes.set(userTypes);

        if (userTypes.length !== 0) {
          this.userType.setValue(userTypes[0].name);
        }
      },
      error: (err) => {
        console.error(err);
      },
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }

  get lastName() {
    return this.form.get('lastName')!;
  }

  get firstName() {
    return this.form.get('firstName')!;
  }

  get email() {
    return this.form.get('email')!;
  }

  get userType() {
    return this.form.get('userType')!;
  }

  onSubmit() {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    // create new user
    const user: User = {
      firstName: this.firstName.value,
      lastName: this.lastName.value,
      email: this.email.value,
      userType: this.userType.value,
    };

    const subscription = this.userApi.addUser(user).subscribe({
      next: () => {
        this.router.navigateByUrl('/users');
      },
      error: (err) => {
        console.error(err);
      },
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
