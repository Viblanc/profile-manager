import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewUserType } from './new-user-type';

describe('NewUserType', () => {
  let component: NewUserType;
  let fixture: ComponentFixture<NewUserType>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [NewUserType]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewUserType);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
