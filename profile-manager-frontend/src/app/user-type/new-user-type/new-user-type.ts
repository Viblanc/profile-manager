import { Component, DestroyRef, inject, signal } from '@angular/core';
import { UserTypeApi } from '../user-type-api';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { notBlank } from '../../shared/custom-validators';

@Component({
  selector: 'app-new-user-type',
  imports: [ReactiveFormsModule],
  templateUrl: './new-user-type.html',
  styleUrl: './new-user-type.css',
})
export class NewUserType {
  private userTypeApi = inject(UserTypeApi);
  private destroyRef = inject(DestroyRef);
  private formBuilder = inject(FormBuilder);
  private router = inject(Router);
  userTypes = signal<UserType[]>([]);
  errorMessage = signal<string>('');
  form = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, notBlank]],
  });

  get name() {
    return this.form.get('name')!;
  }

  onSubmit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const subscription = this.userTypeApi.addUserType(this.name.value).subscribe({
      next: () => {
        this.router.navigateByUrl('user_types');
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
