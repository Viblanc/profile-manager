import { Component, computed, DestroyRef, inject, input, OnInit, signal } from '@angular/core';
import { UserTypeApi } from '../user-type-api';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Router } from '@angular/router';
import { Observable } from 'rxjs';
import { UserType } from '../user-type';
import { notBlank } from '../../../shared/custom-validators';
import { PageTitle } from '../../../core/page-title/page-title';
import { BigButton } from '../../../shared/big-button/big-button';

@Component({
  selector: 'app-new-user-type',
  imports: [ReactiveFormsModule, PageTitle, BigButton],
  templateUrl: './new-user-type.html',
  styleUrl: './new-user-type.css',
})
export class NewUserType implements OnInit {
  private userTypeApi = inject(UserTypeApi);
  private destroyRef = inject(DestroyRef);
  private formBuilder = inject(FormBuilder);
  private router = inject(Router);

  userType = input<UserType>();
  editMode = computed(() => this.userType() !== undefined);
  errorMessage = signal<string>('');
  form = this.formBuilder.nonNullable.group({
    name: ['', [Validators.required, notBlank]],
  });

  ngOnInit(): void {
    this.form.patchValue({
      name: this.userType()?.name,
    });
  }

  get name() {
    return this.form.get('name')!;
  }

  onSubmit(): void {
    // do not send form if invalid
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    if (this.editMode()) {
      this.processRequest(
        this.userTypeApi.editUserType({
          id: this.userType()?.id!,
          name: this.name.value,
        })
      );
    } else {
      this.processRequest(this.userTypeApi.addUserType(this.name.value));
    }
  }

  private processRequest(req: Observable<any>) {
    const subscription = req.subscribe({
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
