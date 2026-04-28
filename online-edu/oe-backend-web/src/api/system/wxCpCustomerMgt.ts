import { ContentTypeEnum } from '@/constants';
import { request } from '@/utils/request';

export function page(params: any) {
  return request.get<any>({
    url: '/admin/bsWxCpCustomer/page',
    params,
  });
}

export function customerDetail(username: any, customerId: any) {
  return request.get<any>({
    url: `/admin/bsWxCpCustomer/customerDetail?username=${username}&customerId=${customerId}`,
  });
}