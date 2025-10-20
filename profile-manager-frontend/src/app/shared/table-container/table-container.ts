import { Component, computed, input, linkedSignal, signal, TemplateRef } from '@angular/core';
import { CommonModule, NgTemplateOutlet } from '@angular/common';
import { TableHeadings } from './table-headings';

@Component({
  selector: 'app-table-container',
  imports: [NgTemplateOutlet, CommonModule],
  templateUrl: './table-container.html',
  styleUrl: './table-container.css',
})
export class TableContainer<T extends { id?: number | string }> {
  // data to display in table
  data = input.required<T[]>();

  // headings
  headings = input.required<TableHeadings<T>>();
  sortableHeadings = computed(() => this.headings().sortable);
  otherHeadings = computed(() => this.headings().other ?? []);

  // keys to access properties of generic type T
  headingsKeys = computed(() => this.headings().keys);

  // template to render rows with
  rowRef = input.required<TemplateRef<any>>();

  // template in case no data is available
  noContentRef = input.required<TemplateRef<any>>();

  // ascending or descending sort
  sortAscending = signal<boolean>(true);

  // selected column on which to sort the table
  selectedColumn = linkedSignal(() => this.headingsKeys()[0]);

  // sorted data based on selected column
  sortedData = computed(() => {
    const sortAsc = this.sortAscending();
    const selectedCol = this.selectedColumn()!;
    const cmp = this.sortableHeadings()[selectedCol]!.cmp!;
    return this.data().sort((a, b) => (sortAsc ? cmp(a, b) : cmp(b, a)));
  });

  sortData(key: keyof T): void {
    if (key === this.selectedColumn()) {
      this.sortAscending.update((old) => !old);
    } else {
      this.sortAscending.set(true);
      this.selectedColumn.set(key);
    }
  }
}
