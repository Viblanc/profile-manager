import { Component, effect, ElementRef, model, output, viewChild } from '@angular/core';

@Component({
  selector: 'app-modal',
  imports: [],
  templateUrl: './modal.html',
  styleUrl: './modal.css',
})
export class Modal {
  modal = viewChild.required<ElementRef<HTMLDialogElement>>('myDialog');
  resourceId = model.required<number>();
  confirm = output<number>();

  constructor() {
    effect(() => {
      if (this.resourceId() !== -1) {
        this.modal().nativeElement.showModal();
      } else {
        this.modal().nativeElement.close();
      }
    });
  }

  closeModal(event: PointerEvent): void {
    const rect = this.modal().nativeElement.getBoundingClientRect();
    const isInside =
      rect.left <= event.clientX &&
      event.clientX <= rect.right &&
      rect.top <= event.clientY &&
      event.clientY <= rect.bottom;

    if (!isInside) {
      this.resourceId.set(-1);
    }
  }
}
