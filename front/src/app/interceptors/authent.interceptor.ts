import { HttpInterceptorFn } from '@angular/common/http';

export const authentInterceptor: HttpInterceptorFn = (req, next) => {
  const authToken = localStorage.getItem('token');

  if (authToken) {
    console.log('token : ' + authToken);
    const authReq = req.clone({
      setHeaders: {
        Authorization: `Bearer ${authToken}`,
      },
    });
    return next(authReq);
  } else {
    return next(req);
  }
};
