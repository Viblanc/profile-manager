import {
  Component,
  inject,
  input,
  output,
  TemplateRef,
  viewChild,
  ViewContainerRef,
} from '@angular/core';
import { UserType } from '../user-type';

@Component({
  selector: 'app-user-type-row',
  imports: [],
  templateUrl: './user-type-row.html',
  styleUrl: './user-type-row.css',
})
export class UserTypeRow {
  userType = input.required<UserType>();
  edit = output<UserType>();
  delete = output<UserType>();
  template = viewChild<TemplateRef<any>>('template');
  private viewContainerRef = inject(ViewContainerRef);

  ngOnInit(): void {
    this.viewContainerRef.createEmbeddedView(this.template()!);
  }
}
