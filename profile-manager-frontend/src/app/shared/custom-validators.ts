import { AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';

export const notBlank: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  return control.value && control.value.trim().length ? null : { isBlank: true };
};
