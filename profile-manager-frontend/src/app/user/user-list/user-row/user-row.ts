import {
  Component,
  inject,
  input,
  OnInit,
  output,
  TemplateRef,
  viewChild,
  ViewContainerRef,
} from '@angular/core';
import { User } from '../../user';

@Component({
  selector: 'app-user-row',
  imports: [],
  templateUrl: './user-row.html',
  styleUrl: './user-row.css',
})
export class UserRow implements OnInit {
  user = input.required<User>();
  view = output<User>();
  edit = output<User>();
  delete = output<number>();
  template = viewChild<TemplateRef<any>>('template');
  private viewContainerRef = inject(ViewContainerRef);

  ngOnInit(): void {
    this.viewContainerRef.createEmbeddedView(this.template()!);
  }
}
