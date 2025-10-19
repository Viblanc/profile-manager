import { ComponentFixture, TestBed } from '@angular/core/testing';

import { UserTypeRow } from './user-type-row';

describe('UserTypeRow', () => {
  let component: UserTypeRow;
  let fixture: ComponentFixture<UserTypeRow>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [UserTypeRow]
    })
    .compileComponents();

    fixture = TestBed.createComponent(UserTypeRow);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
