export type TableHeadings<T> = {
  sortable: {
    [K in keyof T]: {
      title: string;
      cmp: (a: T, b: T) => -1 | 0 | 1;
    };
  };
  other?: string[];
  keys: Array<keyof T>;
};
