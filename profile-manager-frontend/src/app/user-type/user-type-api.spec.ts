import { TestBed } from '@angular/core/testing';

import { UserTypeApi } from './user-type-api';

describe('UserTypeApi', () => {
  let service: UserTypeApi;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(UserTypeApi);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
