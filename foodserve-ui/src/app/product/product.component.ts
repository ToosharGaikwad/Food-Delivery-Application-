import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ProductService } from '../services/product.service';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-product',
  imports: [CommonModule],
  styleUrl: './products.component.css',
  templateUrl: './product.component.html',
})
export class ProductComponent implements OnInit {
  products: any[] = [];
  restaurantId!: number;

  constructor(
    private route: ActivatedRoute,
    private productService: ProductService,
  ) {}

  ngOnInit() {
    this.route.paramMap.subscribe((params) => {
      this.restaurantId = Number(params.get('id'));

      console.log('Restaurant ID:', this.restaurantId);

      if (this.restaurantId) {
        this.loadProducts();
      } else {
        console.error('ID is missing!');
      }
    });
  }

  buyProduct(id: number) {
    console.log('Buying product with id:', id);

    // You can later connect API here
    alert('Product purchased: ' + id);
  }

  loadProducts() {
    this.productService.getProductsByRestaurant(this.restaurantId).subscribe({
      next: (res: any) => {
        console.log(res);
        this.products = res;
      },
      error: (err: any) => {
        console.log(err);
      },
    });
  }
  onSelectRestaurant(id: number) {
    this.productService.getProductsByRestaurant(id).subscribe({
      next: (res: any) => {
        this.products = res;
      },
      error: (err) => {
        console.log(err);
      },
    });
  }
}
