import { Component, computed, DestroyRef, inject, input, OnInit, signal } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { UserApi } from '../user-api';
import { Router, RouterLink } from '@angular/router';
import { Observable } from 'rxjs';
import { User } from '../user';
import { UserType } from '../../user-type/user-type';
import { notBlank } from '../../../shared/custom-validators';
import { PageTitle } from '../../../core/page-title/page-title';
import { BigButton } from '../../../shared/big-button/big-button';

@Component({
  selector: 'app-new-user',
  imports: [ReactiveFormsModule, PageTitle, BigButton, RouterLink],
  templateUrl: './new-user.html',
  styleUrl: './new-user.css',
})
export class NewUser implements OnInit {
  private userApi = inject(UserApi);
  private destroyRef = inject(DestroyRef);
  private formBuilder = inject(FormBuilder);
  private router = inject(Router);

  user = input<User>();
  editMode = computed(() => this.user() !== undefined);
  userTypes = input.required<UserType[]>();
  errorMessage = signal<string>('');
  form = this.formBuilder.nonNullable.group({
    lastName: ['', [Validators.required, Validators.maxLength(64), notBlank]],
    firstName: ['', [Validators.required, Validators.maxLength(64), notBlank]],
    email: ['', [Validators.pattern('^[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,4}$')]],
    userType: ['', [Validators.required]],
  });

  ngOnInit(): void {
    // pre-populate form fields
    if (this.editMode()) {
      this.form.patchValue({
        ...this.user(),
        userType: this.user()?.userType.name,
      });
    } else if (this.userTypes().length) {
      this.form.patchValue({
        userType: this.userTypes()[0].name,
      });
    }
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
    // do not send form if invalid
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    // create new user
    const user: User = {
      id: this.user()?.id,
      firstName: this.firstName.value,
      lastName: this.lastName.value,
      email: this.email.value,
      userType: {
        name: this.userType.value,
      },
    };

    if (this.editMode()) {
      this.processRequest(this.userApi.editUser(user));
    } else {
      this.processRequest(this.userApi.addUser(user));
    }
  }

  private processRequest(req: Observable<any>) {
    const subscription = req.subscribe({
      next: () => {
        this.router.navigateByUrl('users');
      },
      error: (err) => {
        this.errorMessage.set(err.error.message);
        console.error(err);
      },
    });

    this.destroyRef.onDestroy(() => {
      subscription.unsubscribe();
    });
  }
}
